package com.kti.restaurant.suit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.repository.PriceItemRepositoryTests;

@RunWith(Suite.class)
@SuiteClasses({PriceItemRepositoryTests.class})
@TestPropertySource("classpath:test.properties")
public class SuitRepository {


}
