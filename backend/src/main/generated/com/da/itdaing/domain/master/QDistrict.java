package com.da.itdaing.domain.master;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDistrict is a Querydsl query type for District
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDistrict extends EntityPathBase<District> {

    private static final long serialVersionUID = 1991357288L;

    public static final QDistrict district = new QDistrict("district");

    public final com.da.itdaing.global.jpa.QBaseTimeEntity _super = new com.da.itdaing.global.jpa.QBaseTimeEntity(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDistrict(String variable) {
        super(District.class, forVariable(variable));
    }

    public QDistrict(Path<? extends District> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDistrict(PathMetadata metadata) {
        super(District.class, metadata);
    }

}

