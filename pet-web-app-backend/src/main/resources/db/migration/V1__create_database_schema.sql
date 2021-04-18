CREATE TABLE public.role
(
    id   serial                NOT NULL,
    name character varying(30) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

insert into public.role (name)
values ('ROLE_ADMINISTRATOR');
insert into public.role (name)
values ('ROLE_MODERATOR');
insert into public.role (name)
values ('ROLE_EXPERT');
insert into public.role (name)
values ('ROLE_OWNER');

CREATE TABLE public.user
(
    id         bigserial              NOT NULL,
    username   character varying(50)  NOT NULL UNIQUE,
    password   character varying(100) NOT NULL,
    first_name character varying(30)  NOT NULL,
    last_name  character varying(30),
    created    timestamp,
    banned     boolean DEFAULT false,
    deleted    boolean DEFAULT false,
    PRIMARY KEY (id)
);

CREATE TABLE public.user_roles
(
    user_id bigint  NOT NULL,
    role_id integer NOT NULL,
    PRIMARY KEY (user_id, role_id)
);
ALTER TABLE public.user_roles
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;
ALTER TABLE public.user_roles
    ADD CONSTRAINT role_fk FOREIGN KEY (role_id)
        REFERENCES public.role (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;


CREATE TABLE public.pet_expert
(
    id            bigserial              NOT NULL,
    qualification character varying(255) NOT NULL,
    online_help   boolean                NOT NULL,
    user_id       bigint                 NOT NULL,
    reputation    bigint                 NOT NULL,
    confirmed     boolean                NOT NULL DEFAULT false,
    PRIMARY KEY (id)
);
ALTER TABLE public.pet_expert
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.pet
(
    id        bigserial             NOT NULL,
    species   character varying(10) NOT NULL,
    name      character varying(50),
    breed     character varying(20),
    gender    character varying(6),
    birthdate timestamp,
    owner     bigint                NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.pet
    ADD CONSTRAINT owner_fk FOREIGN KEY (owner)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.post
(
    id           bigserial              NOT NULL,
    title        character varying(255) NOT NULL,
    description  character varying(255),
    text         text                   NOT NULL,
    author       bigint                 NOT NULL,
    created_date timestamp,
    rating       bigint                 NOT NULL,
    deleted      boolean                NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.post
    ADD CONSTRAINT author_fk FOREIGN KEY (author)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.post_image
(
    id bigserial NOT NULL,
    post_id bigint NOT NULL,
    image bytea NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.post_image
    ADD CONSTRAINT post_fk FOREIGN KEY (post_id)
        REFERENCES public.post (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.mark
(
    post_id bigint,
    user_id bigint,
    liked boolean NOT NULL,
    PRIMARY KEY (post_id, user_id)
);

ALTER TABLE public.mark
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

ALTER TABLE public.mark
    ADD CONSTRAINT post_fk FOREIGN KEY (post_id)
        REFERENCES public.post (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.ban
(
    id          bigserial         NOT NULL,
    user_id     bigint            NOT NULL,
    description character varying NOT NULL UNIQUE,
    PRIMARY KEY (id)
);
ALTER TABLE public.ban
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.chat
(
    id        bigserial NOT NULL,
    user_id   bigint    NOT NULL,
    expert_id bigint    NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE public.chat
    ADD CONSTRAINT sender_fk FOREIGN KEY (user_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;
ALTER TABLE public.chat
    ADD CONSTRAINT recipient_fk FOREIGN KEY (expert_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.message
(
    id        bigserial                NOT NULL,
    chat_id   bigint                   NOT NULL,
    sender    bigint                   NOT NULL,
    text      text                     NOT NULL,
    timestamp timestamp with time zone NOT NULL,
    status    character varying        NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.message
    ADD CONSTRAINT chat_fk FOREIGN KEY (chat_id)
        REFERENCES public.chat (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;



