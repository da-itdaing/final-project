package com.da.itdaing.domain.master;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPopupCategory is a Querydsl query type for PopupCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPopupCategory extends EntityPathBase<PopupCategory> {

    private static final long serialVersionUID = -825917904L;

    public static final QPopupCategory popupCategory = new QPopupCategory("popupCategory");

    public final com.da.itdaing.global.jpa.QBaseTimeEntity _super = new com.da.itdaing.global.jpa.QBaseTimeEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPopupCategory(String variable) {
        super(PopupCategory.class, forVariable(variable));
    }

    public QPopupCategory(Path<? extends PopupCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPopupCategory(PathMetadata metadata) {
        super(PopupCategory.class, metadata);
    }

}

