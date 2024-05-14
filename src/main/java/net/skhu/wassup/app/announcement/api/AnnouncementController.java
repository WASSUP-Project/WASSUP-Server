package net.skhu.wassup.app.announcement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.announcement.api.dto.RequestAnnouncement;
import net.skhu.wassup.app.announcement.api.dto.ResponseAnnouncement;
import net.skhu.wassup.app.announcement.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/announcements")
@Tag(name = "공지사항 관련 API")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    @Operation(
            summary = "공지사항 작성",
            description = "공지사항을 작성합니다."
    )
    public ResponseEntity<Void> writeAnnouncement(Principal principal, @RequestParam Long id,
                                                  @RequestBody RequestAnnouncement requestAnnouncement) {
        Long adminId = Long.parseLong(principal.getName());
        announcementService.writeAnnouncement(adminId, id, requestAnnouncement);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(
            summary = "공지사항 조회",
            description = "공지사항을 조회합니다."
    )
    public ResponseEntity<List<ResponseAnnouncement>> getAnnouncement(Principal principal, @RequestParam Long id) {
        Long adminId = Long.parseLong(principal.getName());

        return ResponseEntity.ok(announcementService.getAnnouncement(adminId, id));
    }

}