package org.soar.orderservice.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bulk/orders")
@RequiredArgsConstructor
@Slf4j
public class BulkOrderController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String getBulkOrders(@RequestParam("file") MultipartFile file){
        log.info("Uploaded file name {}",file.getOriginalFilename());
        return "Order received successfully";
    }
}
