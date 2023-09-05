package mark1708.com.pastebin.service;

import mark1708.com.pastebin.domain.Paste;
import mark1708.com.pastebin.dto.CreatePasteDto;

public interface PasteService {

  Paste getPasteByHash(String hash);

  Paste create(CreatePasteDto createPasteDto);
}
