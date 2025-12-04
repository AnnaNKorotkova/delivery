INSERT INTO orders (id, order_id, location_x, location_y, status, courier_id)
VALUES ('aa8f5470-bd81-4a42-926f-6910ab320da1', '948035c7-5fd4-465c-aaf4-af198f3621f6', 1, 2, 'ASSIGNED',
        '948035c7-5fd4-465c-aaf4-af198f3621f6'),
       ('aa8f5470-bd81-4a42-926f-6910ab320da2', '948035c7-5fd4-465c-aaf4-af198f3621f7', 3, 4, 'CREATED', null),
       ('aa8f5470-bd81-4a42-926f-6910ab320da3', '948035c7-5fd4-465c-aaf4-af198f3621f8', 5, 6, 'CREATED', null),
       ('aa8f5470-bd81-4a42-926f-6910ab320da4', '948035c7-5fd4-465c-aaf4-af198f3621f9', 9, 8, 'CREATED', null),
       ('aa8f5470-bd81-4a42-926f-6910ab320da5', '948035c7-5fd4-465c-aaf4-af198f362110', 10, 10, 'CREATED', null),
       ('aa8f5470-bd81-4a42-926f-6910ab320da6', '948035c7-5fd4-465c-aaf4-af198f362111', 9, 9, 'CREATED', null);

INSERT INTO couriers (id, courier_id, name, speed)
VALUES ('aa8f5470-bd81-4a42-926f-6910ab320da7', '948035c7-5fd4-465c-aaf4-af198f3621f6', 'Иван', 2),
       ('aa8f5470-bd81-4a42-926f-6910ab320da8', '948035c7-5fd4-465c-aaf4-af198f3621f7', 'Василий', 2),
       ('aa8f5470-bd81-4a42-926f-6910ab320da9', '948035c7-5fd4-465c-aaf4-af198f3621f8', 'Сергей', 2),
       ('aa8f5470-bd81-4a42-926f-6910ab320d10', '948035c7-5fd4-465c-aaf4-af198f3621f9', 'Андрей', 2),
       ('aa8f5470-bd81-4a42-926f-6910ab320d11', '948035c7-5fd4-465c-aaf4-af198f362110', 'Кирилл', 5),
       ('aa8f5470-bd81-4a42-926f-6910ab320d12', '948035c7-5fd4-465c-aaf4-af198f362111', 'Алексей', 5);


INSERT INTO storage_places (storage_place_id,storage_place_name, storage_place_total_volume, order_id, courier_id)
VALUES ('30229876-10fc-4c80-98e3-28f648df1ff2','Рюкзак', 10, 'aa8f5470-bd81-4a42-926f-6910ab320da1',
        'aa8f5470-bd81-4a42-926f-6910ab320da7');