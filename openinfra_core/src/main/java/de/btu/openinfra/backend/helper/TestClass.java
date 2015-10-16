package de.btu.openinfra.backend.helper;




public class TestClass {
	
	public static void main(String[] args) {
		
//		PtLocale l = new PtLocaleDao(
//				UUID.fromString("fd27a347-4e33-4ed7-aebc-eeff6dbf1054"), 
//				OpenInfraSchemas.PROJECT).read(PtLocaleDao.forLanguageTag("de-DE"));
//		System.out.println(l.getId() + " " + l.getCountryCode().getCountryCode());
		
//		List<TopicCharacteristicPojo> list = new TopicCharacteristicDao(
//				UUID.fromString("fd27a347-4e33-4ed7-aebc-eeff6dbf1054"), 
//				OpenInfraSchemas.PROJECT).read(
//						PtLocaleDao.forLanguageTag("de-DE"), "%und%");
//		
//		System.out.println(list.size());
//		for(TopicCharacteristicPojo tcp : list) {
//			System.out.println(tcp.getDescriptions().getLocalizedStrings().get(0).getCharacterString());
//		}
//		
//		List<TopicInstancePojo> list = new TopicInstanceDao(
//				UUID.fromString("fd27a347-4e33-4ed7-aebc-eeff6dbf1054"), 
//				OpenInfraSchemas.PROJECTS).read(
//						PtLocaleDao.forLanguageTag("de-DE"), 
//						UUID.fromString("50d4cb6f-46f1-4422-954c-4c3ec371f063"),
//						"%afsiyeh%",
//						0,
//						30);

//		System.out.println(UUID.fromString(null));
		
//		List<TopicCharacteristicPojo> list = new TopicCharacteristicDao(
//		UUID.fromString("fd27a347-4e33-4ed7-aebc-eeff6dbf1054"), 
//		OpenInfraSchemas.PROJECT).read(
//				PtLocaleDao.forLanguageTag("de-DE"), "%und%");
//		
//		System.out.println(list.size());
//		for(TopicCharacteristicPojo tcp : list) {
//			System.out.println(tcp.getDescriptions().getLocalizedStrings().get(0).getCharacterString());
//		}

//		
//		MultiplicityDao dao = new MultiplicityDao(UUID.fromString("fd27a347-4e33-4ed7-aebc-eeff6dbf1054"), OpenInfraSchemas.PROJECTS);
//		MultiplicityPojo m = dao.read(null, UUID.fromString("09d832bc-2ea8-4874-8f60-9f5878cb89e3"));
//		
//		m.setMax(new Integer(5));
//		System.out.println(m.getTrid());
//		
//		dao.createOrUpdate(m);
		
		// encrypt the raw passwords
//		for(int i = 0; i < 3; i++) {
//			System.out.println("--" + i + "--");
//			System.out.println(OpenInfraRealm.encrypt("root", UUID.fromString("ffdb477f-a0be-44e5-a377-c2d342eb0242")));
//			System.out.println(OpenInfraRealm.encrypt("max", UUID.fromString("eff3a947-6dfc-4bc5-a940-036c7bbc518d")));
//			System.out.println(OpenInfraRealm.encrypt("lieschen", UUID.fromString("07a506f3-0a2c-4b6d-a807-fdd8552315b2")));
//			System.out.println(OpenInfraRealm.encrypt("anonymous", UUID.fromString("966a91eb-328f-4df0-9e66-4dafed830088")));
//			System.out.println(OpenInfraRealm.encrypt("sysadmin", UUID.fromString("e9159662-5767-4efc-adcc-0f9ad9fa0bcb")));
//			System.out.println(OpenInfraRealm.encrypt("syseditor", UUID.fromString("81c23f40-8a3c-46ca-b561-7fc259113d77")));
//			System.out.println(OpenInfraRealm.encrypt("sysguest", UUID.fromString("581d9c8f-3a59-4d49-9aad-f06ad0aa6d16")));
//			System.out.println(OpenInfraRealm.encrypt("projectadmin_baal", UUID.fromString("ca0202b3-3675-46d2-8e59-4d4bd666489a")));
//			System.out.println(OpenInfraRealm.encrypt("projectguest_baal", UUID.fromString("2d38ac64-1242-490b-8569-18477bf18a9c")));
//			System.out.println(OpenInfraRealm.encrypt("projecteditor_baal", UUID.fromString("64573585-8546-429c-98dc-e1b379744d42")));
//			System.out.println(OpenInfraRealm.encrypt("siehstnix", UUID.fromString("db848094-0ead-4214-a850-d3c8cb23971c")));
//		}
		
		System.out.println(System.getProperty("os.name"));
		
	}

}
