package com.hospyboard.api.app.asset.ressources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("asset")
public class AssetResource {

    @GetMapping
    public int getAlertAsset(@RequestParam int mode) throws Exception {
        String path = "/asset/" + (mode == 1 ? "darkmode" : "normal");
        Set<String> list = Stream.of(new File(path).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
        return 1;
    }
}
