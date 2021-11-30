package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.Bartender;

public interface IBartenderService extends IService<Bartender>{
    Bartender findByUserId(Integer userId);
}
