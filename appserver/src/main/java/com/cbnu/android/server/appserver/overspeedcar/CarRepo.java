package com.cbnu.android.server.appserver.overspeedcar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepo extends JpaRepository<OverSpeedCar, Integer> {
    List<OverSpeedCar> findAll();
}
