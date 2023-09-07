package mark1708.com.pastebin.service;

import mark1708.com.pastebin.model.entity.Paste;
import mark1708.com.pastebin.model.dto.CreatePasteDto;

public interface PasteService {

  Paste getPasteByHash(String hash);

  void deleteExpiredPastes();

  Paste create(CreatePasteDto createPasteDto);
}
