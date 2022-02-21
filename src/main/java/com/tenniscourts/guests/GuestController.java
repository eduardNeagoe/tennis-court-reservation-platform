package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/guest")
public class GuestController extends BaseRestController {

    private GuestService guestService;

    @PostMapping("/add")
    public ResponseEntity<Void> addGuest(@Valid @RequestBody CreateGuestRequestDTO createGuestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.save(createGuestRequestDTO).getId())).build();
    }

    @GetMapping("/name/{guestName}")
    public ResponseEntity<GuestDTO> findGuestByName(@PathVariable String guestName) {
        return ResponseEntity.ok(guestService.findByName(guestName));
    }


    @GetMapping("/{guestId}")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findById(guestId));
    }

    @PutMapping("/update")
    public ResponseEntity<GuestDTO> updateGuest(@Valid @RequestBody UpdateGuestRequestDTO updateGuestRequestDTO) {
        return ResponseEntity.ok(guestService.update(updateGuestRequestDTO));
    }

    @DeleteMapping("/delete/{guestId}")
    public ResponseEntity<GuestDTO> deleteGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.delete(guestId));
    }
}
