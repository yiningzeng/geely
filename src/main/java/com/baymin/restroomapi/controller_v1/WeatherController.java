package com.baymin.restroomapi.controller_v1;

import com.baymin.restroomapi.config.okhttp3.MyOkHttpClient;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.RestRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Validated
@Api(description = "天气done")
public class WeatherController {
    /**
     * 天气
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询天气", response = RestRoom.class)
    @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header")
    @GetMapping(value = "/weather")
    public Object getWeather() throws Exception {
        return MyOkHttpClient.getInstance().get("http://t.weather.sojson.com/api/weather/city/101210401");
    }
}
