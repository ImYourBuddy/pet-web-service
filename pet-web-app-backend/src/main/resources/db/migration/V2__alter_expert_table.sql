ALTER TABLE public.pet_expert
    ADD COLUMN confirmed boolean NOT NULL DEFAULT false;
ALTER TABLE public.pet_expert
    ALTER COLUMN deleted SET DEFAULT false;
