package com.kti.restaurant.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kti.restaurant.model.TableReservation;

public interface ITableReservationService extends IService<TableReservation> {
	Page<TableReservation> findAll(Pageable pageable);
}
