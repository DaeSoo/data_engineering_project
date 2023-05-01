package riot.api.data.engineer.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.repository.UserInfoDetailRepository;
import riot.api.data.engineer.service.UserInfoDetailService;

@Service
public class UserInfoDetailImpl implements UserInfoDetailService {
    private final UserInfoDetailRepository userInfoDetailRepository ;

    public UserInfoDetailImpl(UserInfoDetailRepository userInfoDetailRepository){
        this.userInfoDetailRepository = userInfoDetailRepository;
    }

    @Override
    public void UserInfoDetailSave(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            UserInfoDetail userInfoDetail = objectMapper.readValue(response, UserInfoDetail.class);
            userInfoDetailRepository.save(userInfoDetail);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
