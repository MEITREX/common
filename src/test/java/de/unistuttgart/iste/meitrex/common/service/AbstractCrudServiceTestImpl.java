package de.unistuttgart.iste.meitrex.common.service;

import de.unistuttgart.iste.meitrex.common.persistence.IWithId;
import de.unistuttgart.iste.meitrex.common.persistence.MeitrexRepository;
import org.modelmapper.ModelMapper;

public class AbstractCrudServiceTestImpl
        extends AbstractCrudService<Long, AbstractCrudServiceTestImpl.TestEntity, AbstractCrudServiceTestImpl.TestDto> {

    private MeitrexRepository<TestEntity, Long> repository;
    private ModelMapper modelMapper;

    public AbstractCrudServiceTestImpl(MeitrexRepository<TestEntity, Long> repository) {
        this.repository = repository;
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(TestInputDto.class, TestEntity.class)
                .addMappings(mapper -> mapper.skip(TestEntity::setId))
                .addMappings(mapper -> mapper.map(TestInputDto::getInputName, TestEntity::setName));
    }

    @Override
    protected Class<TestEntity> getEntityClass() {
        return TestEntity.class;
    }

    @Override
    protected Class<TestDto> getDtoClass() {
        return TestDto.class;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return modelMapper;
    }

    @Override
    protected MeitrexRepository<TestEntity, Long> getRepository() {
        return repository;
    }

    public static class TestEntity implements IWithId<Long> {
        private Long id;
        private String name;

        public TestEntity() {
        }

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestDto {
        private Long id;
        private String name;

        public TestDto() {
        }

        public TestDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestInputDto {
        private String inputName;

        public TestInputDto(String inputName) {
            this.inputName = inputName;
        }

        public String getInputName() {
            return inputName;
        }

        public TestInputDto setInputName(String inputName) {
            this.inputName = inputName;
            return this;
        }
    }

    public interface TestRepository extends MeitrexRepository<TestEntity, Long> {
    }
}
