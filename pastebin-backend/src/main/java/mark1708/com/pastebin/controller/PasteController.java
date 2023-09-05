package mark1708.com.pastebin.controller;

import lombok.RequiredArgsConstructor;
import mark1708.com.pastebin.converter.PasteConverter;
import mark1708.com.pastebin.dto.CreatePasteDto;
import mark1708.com.pastebin.dto.PasteDto;
import mark1708.com.pastebin.service.PasteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paste")
public class PasteController {

  private final PasteService pasteService;
  private final PasteConverter pasteConverter;

  @GetMapping("/{hash}")
  public ResponseEntity<PasteDto> getPaste(@PathVariable String hash) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(
            pasteConverter.toDto(
                pasteService.getPasteByHash(hash)
            )
        );
  }

  @PostMapping
  public ResponseEntity<String> createPaste(@RequestBody CreatePasteDto createPasteDto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
            pasteService.create(createPasteDto).getHash()
        );
  }
}
