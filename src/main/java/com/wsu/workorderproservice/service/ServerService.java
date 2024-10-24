package com.wsu.workorderproservice.service;

import com.wsu.workorderproservice.dto.ServerDTO;
import com.wsu.workorderproservice.exception.DatabaseErrorException;
import com.wsu.workorderproservice.exception.InvalidRequestException;
import com.wsu.workorderproservice.model.Server;
import com.wsu.workorderproservice.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.wsu.workorderproservice.utilities.CommonUtils.sort;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    /**
     * This method used for retrieve the paginated servers data based on global search
     * @param search - allow to type ahead search by id, firstName, lastName, availabilty
     * @param page - page no.
     * @param rpp - result per page
     * @param sortField - field for sorting the result
     * @param sortOrder - specify order for sorting
     * @return - paginated servers
     */
    public Page<ServerDTO> get(String search, String sortField, String sortOrder, Integer page, Integer rpp) {
        try {
            Page<Object[]> servers = serverRepository.findBySearch(search, PageRequest.of(page-1, rpp, sort(sortField, sortOrder)));
            return servers.map(server -> ServerDTO.builder().id((Integer) server[0]).firstName((String) server[1])
                    .lastName((String) server[2]).availability((String) server[3]).build());
        } catch (Exception e) {
            log.error("Failed to retrieve servers. search:{}, sortField:{}, sortOrder:{}, page:{}, rpp:{}. Exception:",
                    search, sortField, sortOrder, page, rpp, e);
            throw new DatabaseErrorException("Failed to retrieve servers.", e);
        }
    }

    /**
     * This method used for add new server, if it's not exists
     * @param ServerDTO - payload that contains server info
     * @return - saved server entity class
     */
    public ServerDTO save(ServerDTO serverDTO) {
        if (serverRepository.existsById(serverDTO.getId())) {
            throw new InvalidRequestException("server already exist with this id.");
        }
        try {
            return convertToDTO(serverRepository.save(convertToEntity(serverDTO)));
        } catch (Exception e) {
            log.error("Failed to create new server. Exception:", e);
            throw new DatabaseErrorException("Failed to create new server", e);
        }
    }

    /**
     * This method used for update server by server id
     * @param id - id that's used for update server
     * @param ServerDTO - payload that contains server info to be updated
     * @return - updated server dto class
     */
    public ServerDTO update(Integer id, ServerDTO serverDTO) {
        if (!serverRepository.existsById(id)) {
            throw new InvalidRequestException("Invalid server id.");
        }
        try {
            Server server = convertToEntity(serverDTO);
            server.setId(id);
            return convertToDTO(serverRepository.save(server));
        } catch (Exception e) {
            log.error("Failed to update server, serverId:{}. Exception:{}", id, e);
            throw new DatabaseErrorException("Failed to update server", e);
        }
    }

        /**
     * This method used for delete the server by server code
     * @param code - code that's used to delete server
     */
    public void delete(int id) {
        if (!serverRepository.existsById(id)) {
            throw new InvalidRequestException("Invalid server id.");
        }
        try {
            serverRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete server, id:{}. Exception:{}", id, e);
            throw new DatabaseErrorException("Failed to delete server", e);
        }
    }

    /**
     * This method used for convert DTO to entity model class.
     */
    public Server convertToEntity(ServerDTO serverDTO) {
        return Server.builder().firstName(serverDTO.getFirstName())
                .lastName(serverDTO.getLastName()).availability(serverDTO.getAvailability()).build();
    }

    /**
     * This method used for convert Entity model class to DTO
     */
    public ServerDTO convertToDTO(Server server) {
        return ServerDTO.builder().id(server.getId()).firstName(server.getFirstName())
                .lastName(server.getLastName()).availability(server.getAvailability()).build();
    }
}
