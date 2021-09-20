package io.oneprofile.signup.pricer


interface IPricerService {
    fun toSalary(myDailyFee: Int) : Double
}