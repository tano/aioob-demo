package org.springframework.data.mapping.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.annotation.Id
import org.springframework.data.mapping.Association
import org.springframework.data.mapping.context.AbstractMappingContext
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.util.TypeInformation
import java.time.LocalDateTime

class ProblemDemoTests {

    @Test
    internal fun `should not throw`() {
        val context = SampleMappingContext()
        val entity = context.getRequiredPersistentEntity(SampleDataClass::class.java)
        Assertions.assertThatCode {
            ClassGeneratingPropertyAccessorFactory.PropertyAccessorClassGenerator.generateCustomAccessorClass(
                entity
            )
        }.doesNotThrowAnyException()
    }

}

class SamplePersistentProperty internal constructor(
    property: Property?, owner: BasicPersistentEntity<*, SamplePersistentProperty?>?,
    simpleTypeHolder: SimpleTypeHolder?
) :
    AnnotationBasedPersistentProperty<SamplePersistentProperty?>(property!!, owner!!, simpleTypeHolder!!) {
    override fun createAssociation(): Association<SamplePersistentProperty?> {
        return Association(this, null)
    }
}

class SampleMappingContext :
    AbstractMappingContext<BasicPersistentEntity<Any, SamplePersistentProperty?>, SamplePersistentProperty>() {
    override fun <S> createPersistentEntity(
        typeInformation: TypeInformation<S>
    ): BasicPersistentEntity<Any, SamplePersistentProperty?> {
        return BasicPersistentEntity(typeInformation as TypeInformation<Any>)
    }

    override fun createPersistentProperty(
        property: Property,
        owner: BasicPersistentEntity<Any, SamplePersistentProperty?>, simpleTypeHolder: SimpleTypeHolder
    ): SamplePersistentProperty {
        return SamplePersistentProperty(property, owner, simpleTypeHolder)
    }
}

data class SampleDataClass(
    val id: String?,
    val userId: String,
    val status: String,
    val attempts: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val sessionId: String?
) {

    fun copy(
        status: String,
        updatedAt: LocalDateTime,
        sessionId: String,
        attempts: Int = this.attempts
    ) = SampleDataClass(
        this.id,
        this.userId,
        status,
        attempts,
        this.createdAt,
        updatedAt,
        sessionId
    )

}