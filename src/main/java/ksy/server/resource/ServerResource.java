package ksy.server.resource;

import ksy.server.enumeration.Status;
import ksy.server.model.Response;
import ksy.server.model.Server;
import ksy.server.service.impl.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/server")
public class ServerResource {
    private final ServerServiceImpl serverService;

    @GetMapping("/list")
    public ResponseEntity<Response> getServers() {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("servers",serverService.list(30)))
                .message("Servers retrieved")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServers(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("servers",server))
                .message(server.getStatus() == Status.SERVER_UP ? "Ping success" : "Ping failed")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }
//@valid : 엔티티에서 설정한 유효를 확
    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("servers",serverService.create(server)))
                .message("server created")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("servers",serverService.get(id)))
                .message("Server get")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> getServers(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("isDeleted",serverService.delete(id)))
                .message("Servers deleted")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }
    //produces: 출력하고자 하는 데이터의 포
    @GetMapping(path = "/image/{fileName}",produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "Downloads/images/" + fileName));
    }
}
