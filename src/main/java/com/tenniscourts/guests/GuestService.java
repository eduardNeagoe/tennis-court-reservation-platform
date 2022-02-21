package com.tenniscourts.guests;


import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GuestService {

    private GuestRepository guestRepository;
    private GuestMapper guestMapper;

    public GuestDTO findById(Long id) {
        return guestMapper.map(guestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Guest not found!")));
    }


    public GuestDTO findByName(String name) {
        return guestMapper.map(guestRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Guest not found!")));
    }

    public GuestDTO save(CreateGuestRequestDTO createGuestRequestDTO) {
        return guestMapper.map(guestRepository.save(guestMapper.map(createGuestRequestDTO)));
    }


    public GuestDTO delete(Long guestId) {
        Guest guest = guestMapper.map(findById(guestId));
        guestRepository.delete(guest);
        return guestMapper.map(guest);
    }


    public GuestDTO update(UpdateGuestRequestDTO updateGuestRequestDTO) {
        Guest guest = guestMapper.map(findById(updateGuestRequestDTO.getId()));
        guest.setName(updateGuestRequestDTO.getName());
        return guestMapper.map(guestRepository.save(guest));
    }
}
