package com.hospyboard.api.alert.ressources;

import com.hospyboard.api.alert.dto.AlertDTO;
import com.hospyboard.api.alert.dto.FileDto;
import com.hospyboard.api.alert.services.AlertService;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("alert")
public class AlertResource {

    private final AlertService service;

    public AlertResource(AlertService alertService) {
        this.service = alertService;
    }

    @PostMapping
    @Transactional
    public AlertDTO create(@RequestBody AlertDTO request) throws Exception {
        AlertDTO ret = service.create(request);
        service.updateWS();
        return ret;
    }
    @PutMapping
    public AlertDTO update(@RequestBody AlertDTO request) throws  Exception {
        AlertDTO ret = service.update(request);
        service.updateWS();
        return ret;
    }

    @GetMapping
    public Set<AlertDTO> get(@RequestParam String alertUuid) throws Exception {
        Set<AlertDTO> ret = service.get(alertUuid);
        service.updateWS();
        return ret;
    }
    @GetMapping("/all")
    public Set<AlertDTO> getALL() throws Exception {
        return service.get(null);
    }
    @GetMapping("/asset")
    public Set<FileDto> getAlertAsset() throws Exception {
        String path = "services/alert/assets/";
        Set<FileDto> ret = new HashSet<>();

        System.out.println("ici");
        Set<String> list = Stream.of(new File(path).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
        System.out.println("la");
        for (String it : list) {
            FileDto tmp = new FileDto();
            Path p = Paths.get(path + it);
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(p));
            tmp.setFileName(it);
            tmp.setEn(it.replace(".png", ""));
            tmp.setContent(resource);


            ret.add(tmp);
        }
        System.out.println(ret);
        return ret;
    }
}
