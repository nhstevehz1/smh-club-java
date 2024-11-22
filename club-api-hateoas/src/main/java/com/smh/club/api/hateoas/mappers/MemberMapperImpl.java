package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.contracts.mappers.MemberMapper;
import com.smh.club.api.hateoas.models.MemberModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberMapperImpl extends DomainDataMapper implements MemberMapper {
    

    public MemberMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public MemberEntity toEntity(MemberModel model) {
        var entity = modelMapper.map(model, MemberEntity.class);
        entity.setId(0);
        return entity;
    }

    @Override
    public MemberModel toModel(MemberEntity entity) {
        return modelMapper.map(entity, MemberModel.class);
    }

    @Override
    public MemberEntity updateEntity(MemberModel model, MemberEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    @Override
    public List<MemberModel> toModelList(List<MemberEntity> entityList) {
        return mapList(entityList, MemberModel.class);
    }
}
