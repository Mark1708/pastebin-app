package mark1708.com.pastebin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mark1708.com.pastebin.converter.PasteConverter;
import mark1708.com.pastebin.model.dto.CreatePasteDto;
import mark1708.com.pastebin.model.dto.PasteDto;
import mark1708.com.pastebin.service.PasteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
@Tag(name = "Управление заметками")
@RequestMapping("/api/v1/paste")
public class PasteController {

  private final PasteService pasteService;
  private final PasteConverter pasteConverter;

  @GetMapping("/{hash}")
  @Operation(summary = "Получение заметки")
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
  @Operation(summary = "Создание заметки")
  public ResponseEntity<String> createPaste(@RequestBody @Valid CreatePasteDto createPasteDto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
            pasteService.create(createPasteDto).getHash()
        );
  }
}
