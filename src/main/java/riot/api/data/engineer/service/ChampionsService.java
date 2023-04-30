package riot.api.data.engineer.service;


import riot.api.data.engineer.entity.Champions;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;

public interface ChampionsService {

    Champions setChampions(String response);

    String getChampionsInfo(ApiInfo apiInfo, Version version);

}
