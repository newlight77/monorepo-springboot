package io.oneprofile.core.pricer


interface IPricerService {
    fun toSalary(myDailyFee: Int) : Double
}