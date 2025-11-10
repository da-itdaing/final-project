package com.da.itdaing.domain.messaging.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageThread is a Querydsl query type for MessageThread
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessageThread extends EntityPathBase<MessageThread> {

    private static final long serialVersionUID = -959875262L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageThread messageThread = new QMessageThread("messageThread");

    public final com.da.itdaing.domain.user.entity.QUsers admin;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.da.itdaing.domain.user.entity.QUsers seller;

    public final StringPath subject = createString("subject");

    public final NumberPath<Integer> unreadForAdmin = createNumber("unreadForAdmin", Integer.class);

    public final NumberPath<Integer> unreadForSeller = createNumber("unreadForSeller", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMessageThread(String variable) {
        this(MessageThread.class, forVariable(variable), INITS);
    }

    public QMessageThread(Path<? extends MessageThread> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageThread(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageThread(PathMetadata metadata, PathInits inits) {
        this(MessageThread.class, metadata, inits);
    }

    public QMessageThread(Class<? extends MessageThread> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new com.da.itdaing.domain.user.entity.QUsers(forProperty("admin")) : null;
        this.seller = inits.isInitialized("seller") ? new com.da.itdaing.domain.user.entity.QUsers(forProperty("seller")) : null;
    }

}

