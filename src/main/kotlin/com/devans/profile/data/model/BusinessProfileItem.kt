import BusinessProfileItem.Constants.EmptyAddress
import BusinessProfileItem.Constants.EmptyString
import BusinessProfileItem.Constants.TableName
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.devans.profile.model.Address
import com.devans.profile.utils.getCurrentEpochMilliSecond
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@DynamoDBTable(tableName = TableName)
data class BusinessProfileItem(
    @get:DynamoDBHashKey(attributeName = Constants.Attributes.PartitionKey)
    var profileId: String,

    @get:DynamoDBAttribute(attributeName = Constants.Attributes.CompanyName)
    var companyName: String,

    @get:DynamoDBAttribute(attributeName = Constants.Attributes.LegalName)
    var legalName: String,

    @DynamoDBTypeConverted(converter = AddressConverter::class)
    @get:DynamoDBAttribute(attributeName = Constants.Attributes.BusinessAddress)
    var businessAddress: Address,

    @DynamoDBTypeConverted(converter = AddressConverter::class)
    @get:DynamoDBAttribute(attributeName = Constants.Attributes.LegalAddress)
    var legalAddress: Address,

    @get:DynamoDBAttribute(attributeName = Constants.Attributes.TaxIdentifier)
    var taxIdentifier: String,

    @get:DynamoDBAttribute(attributeName = Constants.Attributes.Email)
    var email: String,

    @get:DynamoDBAttribute(attributeName = Constants.Attributes.Website)
    var website: String,

    @get:DynamoDBAttribute(attributeName = Constants.Attributes.CreatedAt)
    var createdAt: Long,

    @get:DynamoDBAttribute(attributeName = Constants.Attributes.UpdatedAt)
    var updatedAt: Long
) {
    constructor() : this(
        profileId = EmptyString,
        companyName = EmptyString,
        legalName = EmptyString,
        businessAddress = EmptyAddress,
        legalAddress = EmptyAddress,
        taxIdentifier = EmptyString,
        email = EmptyString,
        website = EmptyString,
        createdAt = getCurrentEpochMilliSecond(),
        updatedAt = getCurrentEpochMilliSecond()
    )

    object Constants {

        const val TableName = "BusinessProfile"

        const val EmptyString = ""
        val EmptyAddress = Address(
            line1 = EmptyString,
            line2 = EmptyString,
            city = EmptyString,
            state = EmptyString,
            country = EmptyString,
            zip = EmptyString
        )

        object Attributes {
            const val PartitionKey = "profileId"
            const val CompanyName = "companyName"
            const val LegalName = "legalName"
            const val BusinessAddress = "businessAddress"
            const val LegalAddress = "legalAddress"
            const val TaxIdentifier = "taxIdentifier"
            const val Email = "email"
            const val Website = "website"
            const val CreatedAt = "createdAt"
            const val UpdatedAt = "updatedAt"
        }
    }
}

class AddressConverter : DynamoDBTypeConverter<String, Address> {
    override fun convert(address: Address): String =
        jacksonObjectMapper().writeValueAsString(address)

    override fun unconvert(attribute: String?): Address =
        jacksonObjectMapper().readValue(attribute, Address::class.java)
}
