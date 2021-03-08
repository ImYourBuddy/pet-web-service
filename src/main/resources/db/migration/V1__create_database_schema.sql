CREATE TABLE public.roles
(
    id serial NOT NULL,
    name character varying(30) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

insert into public.roles (name) values ('ROLE_ADMINISTRATOR');
insert into public.roles (name) values ('ROLE_MODERATOR');
insert into public.roles (name) values ('ROLE_EXPERT');
insert into public.roles (name) values ('ROLE_OWNER');
insert into public.roles (name) values ('ROLE_READER');

CREATE TABLE public.user
(
    id bigserial NOT NULL,
    username character varying(100) NOT NULL UNIQUE,
    password character varying(255) NOT NULL,
    first_name character varying(30) NOT NULL,
    last_name character varying(30),
    created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    banned boolean DEFAULT false,
    deleted boolean DEFAULT false,
    PRIMARY KEY (id)
);


CREATE TABLE public.user_roles
(
    user_id bigint NOT NULL,
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
        REFERENCES public.roles (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;


CREATE TABLE public.pet_expert
(
    id bigserial NOT NULL,
    qualification character varying(255) NOT NULL,
    online_help boolean NOT NULL,
    user_id bigint NOT NULL,
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
    id bigserial NOT NULL,
    species character varying(10) NOT NULL,
    name character varying(50),
    breed character varying(20),
    gender character varying(6),
    birthdate timestamp,
    owner bigint NOT NULL,
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
    id bigserial NOT NULL,
    title character varying(20) NOT NULL,
    description character varying(40),
    text text NOT NULL,
    author bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.post
    ADD CONSTRAINT author_fk FOREIGN KEY (author)
        REFERENCES public.pet_expert (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

CREATE TABLE public.chat
(
    id bigserial NOT NULL,
    owner_id bigint NOT NULL,
    expert_id bigint NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE public.chat
    ADD CONSTRAINT owner_fk FOREIGN KEY (owner_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;
ALTER TABLE public.chat
    ADD CONSTRAINT expert_fk FOREIGN KEY (expert_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

CREATE TABLE public.message
(
    id bigserial NOT NULL,
    chat_id bigint NOT NULL,
    sender bigint NOT NULL,
    timestamp timestamp with time zone NOT NULL,
    text text NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.message
    ADD CONSTRAINT chat_fk FOREIGN KEY (chat_id)
        REFERENCES public.chat (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

ALTER TABLE public.message
    ADD CONSTRAINT sender_fk FOREIGN KEY (sender)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;
