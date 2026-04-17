package ch.admin.astra.vz.lc.modules.verification.domain.usecase;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UseCaseTest {

    @Test
    void getSortedAttributes_noAttributeGroups() {
        UseCase useCase = UseCase.builder().build();

        assertThat(useCase.getSortedAttributes()).isEmpty();
    }

    @Test
    void getSortedAttributes_attributeGroupsEmpty() {
        UseCase useCase = UseCase.builder()
            .attributeGroups(Collections.emptyList())
            .build();

        assertThat(useCase.getSortedAttributes()).isEmpty();
    }

    @Test
    void getSortedAttributes_oneAttributeGroup_oneAttribute() {
        UseCase useCase = UseCase.builder()
            .attributeGroup(AttributeGroup.builder()
                .attribute(Attribute.builder().order(1L).build())
                .build())
            .build();

        assertThat(useCase.getSortedAttributes()).hasSize(1);
    }

    @Test
    void getSortedAttributes_oneAttributeGroup_multipleAttributes() {
        UseCase useCase = UseCase.builder()
            .attributeGroup(AttributeGroup.builder()
                .attribute(Attribute.builder().name("second").order(10L).build())
                .attribute(Attribute.builder().name("first").order(2L).build())
                .attribute(Attribute.builder().name("third").order(20L).build())
                .build())
            .build();

        List<String> sortedAttributes = useCase.getSortedAttributes();
        assertThat(sortedAttributes).hasSize(3);
        assertThat(sortedAttributes.getFirst()).isEqualTo("first");
        assertThat(sortedAttributes.get(1)).isEqualTo("second");
        assertThat(sortedAttributes.get(2)).isEqualTo("third");
    }

    @Test
    void getSortedAttributes_multipleAttributeGroup() {
        UseCase useCase = UseCase.builder()
            .attributeGroup(AttributeGroup.builder()
                .attribute(Attribute.builder().name("second").order(10L).build())
                .attribute(Attribute.builder().name("fourth").order(100L).build())
                .build())
            .attributeGroup(AttributeGroup.builder()
                .attribute(Attribute.builder().name("third").order(20L).build())
                .build())
            .attributeGroup(AttributeGroup.builder()
                .attribute(Attribute.builder().name("first").order(2L).build())
                .build())
            .build();

        List<String> sortedAttributes = useCase.getSortedAttributes();
        assertThat(sortedAttributes).hasSize(4);
        assertThat(sortedAttributes.getFirst()).isEqualTo("first");
        assertThat(sortedAttributes.get(1)).isEqualTo("second");
        assertThat(sortedAttributes.get(2)).isEqualTo("third");
        assertThat(sortedAttributes.get(3)).isEqualTo("fourth");
    }
}