# java-filmorate
Database ER diagram:

![](ER-diagram.svg "ER diagram")

## SQL command examples

- Get all users:
```SQL
SELECT *
FROM user;
```

- Get mutual friends for users with IDs _id1_ and _id2_:
```SQL
SELECT *
FROM user
WHERE id IN (SELECT friend2_id
            	FROM friendships
            	GROUP BY friend2_id
             	WHERE ARRAY_AGG(friend1_id) = ARRAY[id1,id2]);
```

- Get film with ID _some_id_:
```SQL
SELECT f.id,
    f.name,
    f.description,
    f.releaseDate,
    f.duration,
    r.rating,
    ARRAY_AGG(g.name) 
FROM film AS f
INNER JOIN rating as r ON f.rating_id = r.id
LEFT OUTER JOIN film_genres as fg ON f.id=fg.film_id
INNER JOIN genre AS g on fg.genre_id=g.id
GROUP BY f.id
WHERE f.id=some_id;
```

- Get _N_ most popular films:
```SQL
SELECT f.id,
    f.name,
    f.description,
    f.releaseDate,
    f.duration,
    r.rating,
    ARRAY_AGG(g.name) 
FROM film AS f
INNER JOIN rating as r ON f.rating_id = r.id
LEFT OUTER JOIN film_genres as fg ON f.id=fg.film_id
INNER JOIN genre AS g on fg.genre_id=g.id
GROUP BY f.id
WHERE f.id IN (SELECT film_id
             FROM likes
             GROUP BY film_id
             ORDER By COUNT(user_id) DESC
             LIMIT N);
```