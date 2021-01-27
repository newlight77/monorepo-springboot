package io.tricefal.core.pricer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*
import kotlin.math.roundToInt

@ExtendWith(MockitoExtension::class)
class PricerServiceTest {

    @Mock
    lateinit var adapter: IPricerReferenceAdapter

    lateinit var service: IPricerService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(adapter)
    }

    @Test
    fun `should convert from a daily fee to a pretended salary `() {
        // Arranges
        val pricerRef = PricerReferenceDomain.Builder()
                .lastDate(Instant.now())
                .workDaysPerYear(217)
                .commissionFreelancePercentagePhase1(10)
                .employerChargePercentage(45)
                .monthlyInsurance50(50)
                .restaurantDailyContribution(10)
                .restaurantDailyEmployerPercentage(50)
                .navigoAnnualFee(827)
                .navigoAnnualFeeEmployerPercentage(50)
                .paySlipMonthlyFee(20)
                .build()

        Mockito.`when`(adapter.last()).thenReturn(Optional.of(pricerRef))

        service = PricerService(adapter)

        // Act
        val result = service.toSalary(570)

        // Arrange
        Assertions.assertEquals(74435, result.roundToInt())
    }

}