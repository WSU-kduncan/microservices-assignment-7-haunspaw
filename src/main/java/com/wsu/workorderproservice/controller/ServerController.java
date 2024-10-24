package com.wsu.workorderproservice.controller;

import com.wsu.workorderproservice.dto.ServerDTO;
import com.wsu.workorderproservice.dto.ServiceResponseDTO;
import com.wsu.workorderproservice.service.ServerService;
import com.wsu.workorderproservice.utilities.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.wsu.workorderproservice.utilities.Constants.MESSAGE;
import static com.wsu.workorderproservice.utilities.Constants.PAGE_COUNT;
import static com.wsu.workorderproservice.utilities.Constants.RESULT_COUNT;


/**
 * This controller class created for Server resource CRUD endpoints
 */
@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    /**
     * This method used for retrieve the Servers based on given pagination and filter
     * @param search - allow to filter by customer firstName, lastName, availability
     * @param page - no. of page (default 1)
     * @param rpp - results per page (default 10)
     * @param sortField - sort field for sorting the results (default dateLastUpdated)
     * @param sortOrder - sort order (default desc)
     * @return - list of servers that's matched given criteria
     */
    @GetMapping
    public ResponseEntity<ServiceResponseDTO> getServerss(@RequestParam(required = false) String search,
                                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(required = false, defaultValue = "10") Integer rpp,
                                                  @RequestParam(required = false, defaultValue = "availability") String sortField,
                                                  @RequestParam(required = false, defaultValue = Constants.DESC) String sortOrder) {
        Page<ServerDTO> serverDTOPagePage = serverService.get(search, sortField, sortOrder, page, rpp);
        return new ResponseEntity<>(ServiceResponseDTO.builder().meta(Map.of(MESSAGE, "Servers retrieved successfully.", PAGE_COUNT,
                serverDTOPagePage.getTotalPages(), RESULT_COUNT, serverDTOPagePage.getTotalElements())).data(serverDTOPagePage.getContent())
                .build(), HttpStatus.OK);
    }

    /**
     * This method used for add new server info if it's not exists
     * @param serverDTO - payload that contains server info
     * @return - saved server with HTTP status
     */
    @PostMapping
    public ResponseEntity<ServiceResponseDTO> createCustomer(@RequestBody @Valid ServerDTO serverDTO) {
        return new ResponseEntity<>(ServiceResponseDTO.builder().meta(Map.of(MESSAGE, "Server created successfully"))
                .data(serverService.save(serverDTO)).build(), HttpStatus.OK);
    }

    /**
     * This method used for update server by server id
     * @param id - id of server to update
     * @param serverrDTO -  payload that contains server info that need to be updated
     * @return - updated server with HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponseDTO> updateServer(@PathVariable Integer id, @RequestBody @Valid ServerDTO serverDTO) {
        return new ResponseEntity<>(ServiceResponseDTO.builder().meta(Map.of(MESSAGE, "Customer updated successfully"))
                .data(serverService.update(id, serverDTO)).build(), HttpStatus.OK);
    }

        /**
     * This method used for delete the server by server id
     * @param id - id that's used to delete server
     * @return - HTTP Status of deleted server
     */
    //@DeleteMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod. DELETE),
    //It's used to delete specific resource by id.
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponseDTO> deleteServer(@PathVariable int id) {
        serverService.delete(id);
        return new ResponseEntity<>(ServiceResponseDTO.builder().meta(Map.of(MESSAGE, "Server deleted successfully")).build(), HttpStatus.OK);
    }
}
