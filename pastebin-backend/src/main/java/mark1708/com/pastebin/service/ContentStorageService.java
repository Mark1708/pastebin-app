package mark1708.com.pastebin.service;

public interface ContentStorageService {

  String uploadPasteContent(String hash, String content);

  String getPasteContent(String path);
}
