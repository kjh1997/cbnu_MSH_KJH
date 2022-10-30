package com.cbnu.android.server.appserver.overspeedcar;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarController {
    private final CarRepo carRepo;

    @GetMapping("/CommunityServer/overspeedcar")
    public List<OverSpeedCar> getCarList(HttpServletRequest request, HttpServletResponse response) {
        return carRepo.findAll();
    }

    @PostMapping("/CommunityServer/overspeedcar")
    public String putCarInfo(HttpServletRequest request, HttpServletResponse response) {
        OverSpeedCar car = new OverSpeedCar();
        String data = request.getParameter("data");
        String name = request.getParameter("name");
        car.setData(data);
        car.setName(name);
        carRepo.save(car);
        return "success";
    }

}
