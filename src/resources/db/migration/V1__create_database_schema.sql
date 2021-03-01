CREATE TABLE public.pet_owner
(
    id bigserial NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name character varying(20),
    login character varying(15) NOT NULL,
    password character varying(15) NOT NULL,
    PRIMARY KEY (id)
);

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
        REFERENCES public.pet_owner (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.pet_expert
(
    id bigserial NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name character varying(20) NOT NULL,
    qualification character varying(50) NOT NULL,
    login character varying(15) NOT NULL,
    password character varying(15) NOT NULL,
    online_help boolean NOT NULL,
    PRIMARY KEY (id)
);

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

CREATE TABLE public.moderator
(
    id serial NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name character varying(20) NOT NULL,
    login character varying(15) NOT NULL,
    password character varying(15) NOT NULL,
    admin_privileges boolean NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.chat
(
    id bigserial NOT NULL,
    owner_id bigint NOT NULL,
    expert_id bigint NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE public.chat
    ADD CONSTRAINT owner_fk FOREIGN KEY (owner_id)
        REFERENCES public.pet_owner (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;
ALTER TABLE public.chat
    ADD CONSTRAINT expert_fk FOREIGN KEY (expert_id)
        REFERENCES public.pet_expert (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

CREATE TABLE public.message
(
    id bigserial NOT NULL,
    chat_id bigint NOT NULL,
    "from" bigint NOT NULL,
    "timestamp" timestamp with time zone NOT NULL,
    text text NOT NULL,
    PRIMARY KEY (id)
);
