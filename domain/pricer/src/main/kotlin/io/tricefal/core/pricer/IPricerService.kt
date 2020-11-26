package io.tricefal.core.pricer


interface IPricerService {
    fun toSalary(myDailyFee: Int) : Double
}