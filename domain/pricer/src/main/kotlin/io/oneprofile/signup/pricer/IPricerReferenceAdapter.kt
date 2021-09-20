package io.oneprofile.signup.pricer

import java.util.*

interface IPricerReferenceAdapter {
    fun save(pricerRef: PricerReferenceDomain): PricerReferenceDomain
    fun last(): Optional<PricerReferenceDomain>
}