package mark1708.com.pastebin.converter;

import lombok.RequiredArgsConstructor;
import mark1708.com.pastebin.model.entity.Paste;
import mark1708.com.pastebin.model.entity.Tag;
import mark1708.com.pastebin.model.dto.PasteDto;
import mark1708.com.pastebin.service.ContentStorageService;
import mark1708.com.pastebin.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasteConverter {

  private final TagService tagService;
  private final ContentStorageService contentStorageService;

  public PasteDto toDto(Paste paste) {
    PasteDto pasteDto = new PasteDto();
    BeanUtils.copyProperties(paste, pasteDto, "tags");

    pasteDto.setTags(
        tagService.findAllByPasteHash(paste.getHash())
            .stream()
            .map(Tag::getName)
            .toList()
    );
    pasteDto.setContent(contentStorageService.getPasteContent(paste.getContentPath()));
    return pasteDto;
  }
}
