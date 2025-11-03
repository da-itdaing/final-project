package com.da.itdaing.domain.master;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QConsumerCategory is a Querydsl query type for ConsumerCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConsumerCategory extends EntityPathBase<ConsumerCategory> {

    private static final long serialVersionUID = 1583699182L;

    public static final QConsumerCategory consumerCategory = new QConsumerCategory("consumerCategory");

    public final com.da.itdaing.global.jpa.QBaseTimeEntity _super = new com.da.itdaing.global.jpa.QBaseTimeEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QConsumerCategory(String variable) {
        super(ConsumerCategory.class, forVariable(variable));
    }

    public QConsumerCategory(Path<? extends ConsumerCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConsumerCategory(PathMetadata metadata) {
        super(ConsumerCategory.class, metadata);
    }

}

