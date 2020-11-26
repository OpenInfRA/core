package de.btu.openinfra.backend.rest.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.tika.Tika;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;
import org.json.simple.JSONObject;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.common.hash.Hashing;

import de.btu.openinfra.backend.OpenInfraProperties;
import de.btu.openinfra.backend.OpenInfraPropertyKeys;
import de.btu.openinfra.backend.OpenInfraPropertyValues;
import de.btu.openinfra.backend.db.daos.file.FileDao;
import de.btu.openinfra.backend.db.daos.file.FilesProjectDao;
import de.btu.openinfra.backend.db.daos.file.SupportedMimeTypeDao;
import de.btu.openinfra.backend.db.jpa.model.file.SupportedMimeType;
import de.btu.openinfra.backend.db.pojos.file.FilePojo;
import de.btu.openinfra.backend.db.pojos.file.FilesProjectPojo;
import de.btu.openinfra.backend.db.rbac.OpenInfraHttpMethod;
import de.btu.openinfra.backend.db.rbac.file.FileRbac;
import de.btu.openinfra.backend.db.rbac.rbac.SubjectRbac;
import de.btu.openinfra.backend.exception.OpenInfraWebException;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

/**
 * This class represents the main component of the file service. It is used to
 * view/manage files.
 * <br/>
 * File type of thumbnails is PNG. This is currently not configurable.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
//TODO Move the logic from the resource file to the DAO layer.
@Path("/v1/files")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class FileResource {

	private static final String THUMB_TYPE = "png";
	private static final String EXTENSION = "." + THUMB_TYPE;

	/**
	 * Delivers the count of uploaded files of the current user.
	 *
	 * @param uriInfo
	 * @param request
	 * @return user-specific count of uploaded files
	 */
	@GET
    @Path("count")
	@Produces({MediaType.TEXT_PLAIN})
    public long getFilesCountBySubject(
    		@Context UriInfo uriInfo,
    		@Context HttpServletRequest request) {
		return new FileRbac().getFilesCountBySubject(
				OpenInfraHttpMethod.valueOf(request.getMethod()), uriInfo);
	}

	/**
	 * Delivers a list of file information uploaded by the current user. The
	 * content of the file is not transmitted. This resource is paging enabled.
	 *
	 * @param uriInfo
	 * @param request
	 * @param offset the number where to start
	 * @param size the max. length of the list
	 * @return A user-specific list of file information.
	 */
	@GET
	public List<FilePojo> readFilesBySubject(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new FileRbac().readBySubject(
				OpenInfraHttpMethod.valueOf(request.getMethod()),
				uriInfo, offset, size);
	}

	/**
	 * Delivers a specific file information.
	 *
	 * @param uriInfo
	 * @param request
	 * @param fileId the id of the requested file
	 * @return a specific file information
	 */
	@GET
	@Path("{fileId:([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})}")
	public FilePojo getFile(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@PathParam("fileId") UUID fileId) {
		// Return the file, when the current user is owner of this file
		FilePojo pojo = new FileDao().read(null, fileId);
		if(new SubjectRbac().self().getUuid().equals(pojo.getSubject())) {
			return pojo;
		}

		// Set the required permission
		String requiredPermission = "/projects/{id}:" +
			OpenInfraHttpMethod.valueOf(request.getMethod()).getAccess() + ":";
		// Get the list of projects where this file belongs to
		List<FilesProjectPojo> fpPojos =
				new FilesProjectDao().read(null, fileId, 0, Integer.MAX_VALUE);
		for(FilesProjectPojo fpp : fpPojos) {
			// Return the file when the subject is permitted
			if(SecurityUtils.getSubject().isPermitted(
					requiredPermission + fpp.getProject())) {
				return pojo;
			}
		}
		throw new WebApplicationException(Status.FORBIDDEN);
	}

	/**
	 * This method deletes an existing file (including the thumbnails). First,
	 * the file reference is deleted from the users space. If there still exists
	 * a reference to another user, the file isn't deleted physically. It is
	 * still available. If there is no other reference it is deleted from disk.
	 * <br/>
	 * TODO Revise the delete method in the future. It should also be evaluated
	 * if the file object is used in a view (e.g. as project title image).
	 *
	 * @param uriInfo
	 * @param request
	 * @param fileId the id of the file
	 * @return the uuid of the deleted file
	 */
	@DELETE
	@Path("{fileId:([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})}")
	public Response delete(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@PathParam("fileId") UUID fileId) {

		// TODO Security should be part of RBAC!
		if(!SecurityUtils.getSubject().isPermitted("/files:w")) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		FileDao dao = new FileDao();
		FilePojo fp = dao.read(null, fileId);
		boolean del = new FileDao().delete(
				fileId, new SubjectRbac().self().getUuid());
		if(del && dao.countBySignature(fp.getSignature()) == 0) {
			deleteFile(fp);
		}
		return OpenInfraResponseBuilder.deleteResponse(del, fileId);
	}

	/**
	 * This resource delivers the content of a file when the current user has
	 * read access to the project where this file is related to.
	 * <br/>
	 * Possible dimensions (dim) are currently:
	 * <ul>
	 * 	<li>thumbnail</li>
	 *  <li>middle</li>
	 *  <li>popup</li>
	 * </ul>
	 * The size of these dimensions can be configured in the
	 * OpenInfRA.properties file.
	 *
	 * @param uriInfo
	 * @param request
	 * @param fileId the id of the requested file
	 * @param dim the size of the file
	 * @return the file content
	 */
	@GET
	@Path("{fileId:([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})}/{dim}")
	public Response getFileContent(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@PathParam("fileId") UUID fileId,
			@PathParam("dim") String dim) {
		FilePojo pojo = getFile(uriInfo, request, fileId);
		String filePath = "";
		String fileExtension = ".png";
		if(dim.equalsIgnoreCase("origin")) {
			filePath = OpenInfraPropertyValues.UPLOAD_PATH.getValue();
			fileExtension = "." + FilenameUtils.getExtension(
					pojo.getOriginFileName());
		} else if(dim.equalsIgnoreCase("thumbnail")) {
			filePath = OpenInfraPropertyValues.IMAGE_THUMBNAIL_PATH.getValue();
		} else if(dim.equalsIgnoreCase("middle")) {
			filePath = OpenInfraPropertyValues.IMAGE_MIDDLE_PATH.getValue();
		} else if(dim.equalsIgnoreCase("popup")) {
			filePath = OpenInfraPropertyValues.IMAGE_POPUP_PATH.getValue();
		}
		String fileName = filePath + pojo.getSignature() + fileExtension;
		// Send not found status code when file desn't exists
		if(!new File(fileName).exists()) {
			Response.status(Status.NOT_FOUND).build();
		}
		java.nio.file.Path p = Paths.get(fileName);
		byte[] document = null;
		String contentType = "";
		try {
			document = Files.readAllBytes(p);
			contentType = Files.probeContentType(p);
		} catch (IOException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return Response.ok(document, contentType)
				.header("content-disposition","attachment; filename = " +
						FilenameUtils.removeExtension(
								pojo.getOriginFileName()) +
								fileExtension).build();
	}

	/**
	 * This resource uploads a file to the server. Only configured mime types
	 * are supported. The server checks the mime type by its own routine. The
	 * file extension isn't used to identify the file's mime type.
	 * <br/>
	 * The file is processed as follows:
	 * <ol>
	 * 	<li>Mime type check (unsupported mime types are rejected)</li>
	 *  <li>Signature creation in order to detect duplicate entries</li>
	 *  <li>Thumbnail generation</li>
	 *  <li>Export of EXIF data</li>
	 *  <li>Creation of a reference to the user</li>
	 * </ol>
	 * <br/>
	 * If duplicate is detected this method returns the related POJO in any
	 * case. However, the generation of thumbnails, the export of EXIF data and
	 * the creation of a reference to the user are left out when a duplicate
	 * was detected.
	 * <br/>
	 * You'll need to install ufraw under Linux-based OS when you want to
	 * support DNG images.
	 * <br/>
	 * You'll need to install ghostview in order to support PDF thumbnail
	 * generation.
	 *
	 * @param uriInfo
	 * @param request
	 * @param originFileName the current file name
	 * @param is the file content
	 * @return the file POJO of the newly created file
	 */
	@POST
	@Path("/upload/{originFileName}")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public FilePojo uploadFile(
    		@Context UriInfo uriInfo,
    		@Context HttpServletRequest request,
    		@PathParam("originFileName") String originFileName,
    		InputStream is) {

		if(originFileName == null ||
				FilenameUtils.getExtension(originFileName) == null) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		if(!SecurityUtils.getSubject().isPermitted("/files:w")) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		// Get the current user id and create a list to hold uploaded files
		UUID subject = new SubjectRbac().self().getUuid();

		FilePojo pojo = new FilePojo();
    	String signature = "";
    	String fileName = "";
    	File currentFile = null;
    	java.nio.file.Path filePath = null;
    	FilePojo returnPojo = null;
		boolean acceptedMimeType = false;
		// Handle file stream
		try {
			fileName = saveFile(is, originFileName);
			filePath = Paths.get(fileName);
			currentFile = new File(fileName);
			// Force to set the mime type when the mime type is unknown by the
			// OS in order to avoid null values (e.g. DNG files are sometimes
			// handled as tiff images)
			pojo.setMimeType(Files.probeContentType(filePath));
			if(pojo.getMimeType() == null) {
				pojo.setMimeType(new Tika().detect(filePath));
			}
		} catch (IOException ex) {
			throw new OpenInfraWebException(ex);
		}

		// Evaluate the mime type of the current file
		try {
			List<SupportedMimeType> mimeTypes =
					new SupportedMimeTypeDao().read();
			MimeType currentMimeType = new MimeType(pojo.getMimeType());
			for(SupportedMimeType smt : mimeTypes) {
				if(acceptedMimeType) {
					break;
				}
				acceptedMimeType = currentMimeType.match(smt.getMimeType());
			}
		} catch (MimeTypeParseException ex) {
			throw new OpenInfraWebException(ex);
		}

		// Handle unaccepted mime types
		if(!acceptedMimeType) {
			currentFile.delete();
			throw new WebApplicationException(
					Status.UNSUPPORTED_MEDIA_TYPE);
		}

		// Generate signature/hash
		try {
			signature = com.google.common.io.Files.asByteSource(new File(fileName)).hash(Hashing.sha256()).toString();
		} catch (IOException ex) {
			throw new OpenInfraWebException(ex);
		}

		// rename file
		File signatureFile = new File(
				OpenInfraPropertyValues.UPLOAD_PATH.getValue() +
				signature + "." + FilenameUtils.getExtension(fileName));
		if(signatureFile.exists()) {
			currentFile.delete();
		} else {
			currentFile.renameTo(signatureFile);
		}

		pojo.setSignature(signature);
		pojo = resizeDimensions(signatureFile.getAbsolutePath(), pojo);
		try {
			pojo.setExifData(extractMetadata(signatureFile));
		} catch (Exception e) {
			System.err.append("Metadata Extractor: " + e.getMessage());
		}
    	pojo.setOriginFileName(originFileName);
    	pojo.setSubject(subject);
    	FileRbac rbac = new FileRbac();
    	UUID result = null;
    	// Try to insert the uploaded file into the database. If the uploaded
    	// file is a duplicate return the POJO by selecting by signature.
    	try {
        	result = rbac.createOrUpdate(
        			OpenInfraHttpMethod.valueOf(request.getMethod()),
        			uriInfo, null, pojo);
    	} catch(OpenInfraWebException ex) {
    		System.err.println("FileResource: Probably duplicate file key "
    				+ "detected!");
    		returnPojo = rbac.readBySubjectAndSignature(
    				OpenInfraHttpMethod.valueOf(request.getMethod()),
    				uriInfo, subject, signature);
    	}
    	if(returnPojo == null) {
    		returnPojo = rbac.read(
     			OpenInfraHttpMethod.valueOf(request.getMethod()),
     			uriInfo, null, result);
    	}
		return returnPojo;
	}


	/**
	 * A resource to support URL-based file upload. The URL should be URL
	 * encoded!
	 *
	 * @param fileurl the URL of the file URL encoded
	 * @return a file POJO
	 */
	@POST
	@Path("/urlupload")
	public FilePojo uploadUrlFile(
    		@Context UriInfo uriInfo,
    		@Context HttpServletRequest request,
    		@QueryParam("fileurl") String fileurl) {
		String fileName = FilenameUtils.getBaseName(fileurl);
		String extension = FilenameUtils.getExtension(fileurl);
		if(extension != null && !extension.equals("")) {
			fileName += "." + extension;
		}
	    try {
			return uploadFile(uriInfo, request, fileName,
					new URL(fileurl).openStream());
		} catch (IOException e) {
			throw new OpenInfraWebException(e);
		}
	}

	/**
	 * This method saves a file into the file system. The path is specified
	 * by the upload path variable. The file name is used to extract the file
	 * extension. A UUID is generated in order to store the file into the file
	 * system.
	 *
	 * @param fileStream the file content as stream
	 * @param fileName the file name
	 * @return the path of the file
	 */
	private String saveFile(InputStream fileStream, String fileName)
			throws IOException {
		String filePath = OpenInfraPropertyValues.UPLOAD_PATH.getValue() +
				UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
		try {
			OutputStream os = new FileOutputStream(new File(filePath));
			int read = 0;
			byte[] buffer = new byte[1024];
			while((read = fileStream.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			os.flush();
			os.close();
		} catch (Exception ex) {
			throw new OpenInfraWebException(ex);
		} finally {
			if(fileStream != null) {
				fileStream.close();
			}
		}
		return filePath;
	}

	/**
	 * This method deletes all files (including the thumbnails) concerning the
	 * POJO.
	 *
	 * @param pojo the corresponding POJO object
	 */
	private void deleteFile(FilePojo pojo) {
		File origin = new File(
				OpenInfraPropertyValues.UPLOAD_PATH.getValue() +
				pojo.getSignature() + "." +
						FilenameUtils.getExtension(pojo.getOriginFileName()));
		if(origin.exists()) {
			origin.delete();
		}

		File thumbnail = new File(
				OpenInfraPropertyValues.IMAGE_THUMBNAIL_PATH.getValue() +
				pojo.getSignature() + EXTENSION);
		if(thumbnail.exists()) {
			thumbnail.delete();
		}

		File middle = new File(
				OpenInfraPropertyValues.IMAGE_MIDDLE_PATH.getValue() +
				pojo.getSignature() + EXTENSION);
		if(middle.exists()) {
			middle.delete();
		}

		File popup = new File(
				OpenInfraPropertyValues.IMAGE_POPUP_PATH.getValue() +
				pojo.getSignature() + EXTENSION);
		if(popup.exists()) {
			popup.delete();
		}
	}

	/**
	 * This method is used to create a set of resized images of the uploaded
	 * file. Therefore, ImageMagick is used. It will create the corresponding
	 * resized images when it is possible.
	 * <br/>
	 * In order to provide DNG images you need to install ufraw under linux.
	 *
	 * @param file the path to the file
	 * @param pojo the corresponding pojo object
	 * @return the file pojo
	 */
	private FilePojo resizeDimensions(String file, FilePojo pojo) {

		ConvertCmd cmd = new ConvertCmd();

		// Thumbnail
		String[] thumbDim = OpenInfraProperties.getProperty(
				OpenInfraPropertyKeys.IMG_THUMBNAIL_DIMENSION.getKey())
				.split("x");
		String thumbPath =
				OpenInfraPropertyValues.IMAGE_THUMBNAIL_PATH.getValue() +
				pojo.getSignature() + EXTENSION;
		File thumbFile = new File(thumbPath);

		// Middle sized image
		String[] middleDim = OpenInfraProperties.getProperty(
				OpenInfraPropertyKeys.IMG_MIDDLE_DIMENSION.getKey())
				.split("x");
		String middlePath =
				OpenInfraPropertyValues.IMAGE_MIDDLE_PATH.getValue() +
				pojo.getSignature() + EXTENSION;
		File middleFile = new File(middlePath);

		// Popup sized image
		String[] popupDim = OpenInfraProperties.getProperty(
				OpenInfraPropertyKeys.IMG_POPUP_DIMENSION.getKey())
				.split("x");
		String popupPath =
				OpenInfraPropertyValues.IMAGE_POPUP_PATH.getValue() +
				pojo.getSignature() + EXTENSION;
		File popupFile = new File(popupPath);

		try {
			String originGeom = new Info(file, true).getImageGeometry();
			pojo.setOriginDimension(
					originGeom.substring(0, originGeom.indexOf("+")));

			if(!popupFile.exists()) {
				cmd.run(opBuilder(popupDim, file,
						pojo.getMimeType(), popupPath, false));
			}
			if(popupFile.exists()) {
				String popupGeom = new Info(popupPath, true).getImageGeometry();
				pojo.setPopupDimension(
						popupGeom.substring(0, popupGeom.indexOf("+")));
			}

			if(!middleFile.exists() && new File(popupPath).exists()) {
				cmd.run(opBuilder(middleDim, popupPath,
						"image/png", middlePath, false));
			}
			if(middleFile.exists()) {
				String middleGeom =
						new Info(middlePath, true).getImageGeometry();
				pojo.setMiddleDimension(
						middleGeom.substring(0, middleGeom.indexOf("+")));
			}

			if(!thumbFile.exists() && new File(popupPath).exists()) {
				cmd.run(opBuilder(thumbDim, popupPath,
						"image/png", thumbPath, true));
			}
			if(thumbFile.exists()) {
				String thumbGeom = new Info(thumbPath, true).getImageGeometry();
				pojo.setThumbnailDimension(
						thumbGeom.substring(0, thumbGeom.indexOf("+")));
			}

		} catch (IOException | InterruptedException | IM4JavaException e) {
			System.err.println("ImageMagick: " + e.getMessage());
		}
		return pojo;
	}

	/**
	 * This method is a simple ImageMagick operation builder.
	 *
	 * @param dimensions
	 * @param filePath
	 * @param mimeType
	 * @param destination
	 * @param isThumbnail
	 * @return the ImageMagick operation
	 */
	private IMOperation opBuilder(
			String[] dimensions, String filePath,
			String mimeType, String destination, boolean isThumbnail) {

		IMOperation op = new IMOperation();

		if(isThumbnail) {
			op.thumbnail(Integer.valueOf(dimensions[0]),
					Integer.valueOf(dimensions[1]),"!");
		} else {
			op.thumbnail(Integer.valueOf(dimensions[0]),
					Integer.valueOf(dimensions[1]));
		}
		if(mimeType != null && mimeType.contains("pdf")) {
			op.addImage(filePath + "[0]");
			op.background("white");
			op.alpha("remove");
		} else {
			op.addImage(filePath);
		}
		op.addImage(destination);
		return op;
	}

	/**
	 * This method extracts the metadat of a file when present.
	 *
	 * @param file the file from which the data has to be extracted
	 * @return the meta data as JSON string
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String extractMetadata(File file) throws Exception {
		Metadata md = null;
		md = ImageMetadataReader.readMetadata(file);

		JSONObject json = new JSONObject();
		Map<String, Map<String, String>> dirMap =
				new HashMap<String, Map<String, String>>();
		for(Directory dir : md.getDirectories()) {
			Map<String, String> tags = new HashMap<String, String>();
			for(Tag tag : dir.getTags()) {
				tags.put(tag.getTagName(), tag.getDescription());
			}
			dirMap.put(dir.getName(), tags);
		}
		json.putAll(dirMap);
		return json.toJSONString();
	}

}
