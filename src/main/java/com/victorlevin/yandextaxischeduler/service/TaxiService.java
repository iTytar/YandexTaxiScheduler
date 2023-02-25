package com.victorlevin.yandextaxischeduler.service;

import com.victorlevin.yandextaxischeduler.apiclient.TaxiApiClient;
import com.victorlevin.yandextaxischeduler.model.Coordinate;
import com.victorlevin.yandextaxischeduler.model.MomentPrice;
import com.victorlevin.yandextaxischeduler.model.Option;
import com.victorlevin.yandextaxischeduler.model.Price;
import com.victorlevin.yandextaxischeduler.properties.YandexProperties;
import com.victorlevin.yandextaxischeduler.repository.PriceRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaxiService {
    private final TaxiApiClient taxiApiClient;
    private final PriceRepository priceRepository;
    private final YandexProperties yandexProperties;
    private AtomicInteger price;


    public TaxiService(TaxiApiClient taxiApiClient, PriceRepository priceRepository, MeterRegistry meterRegistry, YandexProperties yandexProperties) {
        this.taxiApiClient = taxiApiClient;
        this.priceRepository = priceRepository;
        this.yandexProperties = yandexProperties;
        price = new AtomicInteger();
        meterRegistry.gauge("price", price);
    }

    @Transactional
    @Timed("gettingPriceFromClient")
    public void getPrice(Coordinate startPoint, Coordinate endPoint) {
        String rll = startPoint.toString() + "~" + endPoint.toString();

//        Price currentPrice = taxiApiClient.getPrice(yandexProperties.getClid(), yandexProperties.getApiKey(), rll);
        Price currentPrice = getPrice();
        if(currentPrice.options.isEmpty()) {
            throw new RuntimeException("options null");
        }

        double priceDouble = currentPrice.options.get(0).getPrice();
        price.set((int) priceDouble);

        MomentPrice momentPrice = new MomentPrice(
                LocalDateTime.now(ZoneId.of("Asia/Yerevan")),
                priceDouble);
        priceRepository.save(momentPrice);
    }

    public List<MomentPrice> getAllPrices() {
        return priceRepository.findAll();
    }
    
    private Price getPrice() {
        final double min = 240.0;
        final double max = 580.0;
        Random random = new Random();

        try {
            Thread.sleep(random.nextLong(100L, 500L));
        } catch (InterruptedException ex) {
        }
        Option option = new Option();
        option.setPrice(random.nextDouble(min, max));
        Price price = new Price();
        price.setOptions(List.of(option));
        return price;
    }
    
}
