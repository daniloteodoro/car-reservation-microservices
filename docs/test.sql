-- 2018-08-25

-- Return reservations associated to category COMPACT in a given period
select r.pickupdatetime, r.dropoffdatetime
  from reservation r
 where r.category_category_type = 'COMPACT'
   and (('2019-06-01 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
	('2019-06-18 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
	('2019-06-01 17:00:00' <= r.pickupdatetime and '2019-06-18 17:00:00' >= r.dropoffdatetime))




-- 2019-08-19

-- Get count of cars in a certain category
 select * 
   from car c
   inner join model m on m.id = c.model_id
  where m.category_id = 'BASIC';

-- Return amount of reservations per category
select c.category_type, coalesce(reservations.amount, 0) as total_reservations
   from category c
   left join (select r.category_category_type, count(*) as amount
			    from reservation r
			   where (('2019-06-01 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
				      ('2019-06-18 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
					  ('2019-06-01 17:00:00' <= r.pickupdatetime and '2019-06-18 17:00:00' >= r.dropoffdatetime))
			   group by r.category_category_type) reservations
					  on reservations.category_category_type = c.category_type;

-- Amount of cars reserved in a specific category in a certain period
-- select r.id, r.pickupdatetime, r.dropoffdatetime, r.reservation_number
--   from category c
--   inner join reservation r on r.category_category_type = c.category_type
-- --  where (('2019-06-14 08:30:00' < r.pickupdatetime and '2019-06-14 08:30:00' < r.pickupdatetime) or
-- -- 		('2019-06-16 09:30:00' > r.dropoffdatetime and '2019-06-16 09:30:00' < r.dropoffdatetime))
--  where (('2019-06-01 18:00:00' between r.pickupdatetime and r.dropoffdatetime) or
-- 		('2019-07-31 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
-- 	    ('2019-06-01 18:00:00' <= r.pickupdatetime and '2019-07-31 17:00:00' >= r.dropoffdatetime))
--    and c.category_type = 'BASIC'
--  order by r.pickupdatetime;

select available.total - unavailable.total as total_available
from
(select count(*) as total
   from car c
  inner join model m on m.id = c.model_id
 where m.category_id = 'BASIC') available,

(select count(*) as total
   from category c
  inner join reservation r on r.category_category_type = c.category_type
 where (('2019-06-01 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
		('2019-06-17 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
	    ('2019-06-01 17:00:00' <= r.pickupdatetime and '2019-06-17 17:00:00' >= r.dropoffdatetime))
   and c.category_type = 'BASIC') unavailable

-- "2019-06-17 08:30:00"	"2019-06-23 17:30:00"
-- "2019-06-18 08:30:00"	"2019-06-20 17:30:00"
-- "2019-06-19 08:30:00"	"2019-06-20 17:30:00"
-- "2019-06-23 08:30:00"	"2019-06-30 17:30:00"

Running version:
select c.*, coalesce(available.total, 0) as "totalAvailable", coalesce(reservations.amount, 0) as "totalReserved"
from category c
left join (select m.category_id, count(*) as total
from car c
inner join model m on m.id = c.model_id
group by m.category_id) available on available.category_id = c.category_type
left join (select r.category_category_type, count(*) as amount
from reservation r
where (('2019-06-01 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
('2019-06-18 17:00:00' between r.pickupdatetime and r.dropoffdatetime) or
('2019-06-01 17:00:00' <= r.pickupdatetime and '2019-06-18 17:00:00' >= r.dropoffdatetime))
group by r.category_category_type) reservations
on reservations.category_category_type = c.category_type
order by c.category_type



-------------> old <---------------


-- "BASIC cars available from 2019-06-18 to 2019-06-21: 0"
-- Get count of cars in a certain category
select * 
  from car c
  inner join model m on m.id = c.model_id
 where m.category_id = 'BASIC';
 
-- From the cars above, get the ones available for the intended period of 2019-06-18 to 2019-06-21
select * 
  from car c
  inner join model m on m.id = c.model_id
 where m.category_id = 'BASIC'
   and (
   );

-- Amount of cars reserved in a specific category
select r.id, r.pickupdatetime, r.dropoffdatetime
  from category c
  inner join reservation r on r.category_category_type = c.category_type
 where r.pickupdatetime <= '2019-06-21 17:30:00'  
   and r.dropoffdatetime >= '2019-06-18 08:30:00'
   and c.category_type = 'BASIC'
 order by r.id;

-- Total available
select available.total - unavailable.total as total_available
from
(select count(*) as total
  from car c
  inner join model m on m.id = c.model_id
 where m.category_id = 'BASIC') available,
 
(select count(*) as total
  from category c
  inner join reservation r on r.category_category_type = c.category_type
 where r.pickupdatetime <= '2019-06-21 17:30:00'  
   and r.dropoffdatetime >= '2019-06-18 08:30:00'
   and c.category_type = 'BASIC') unavailable

-- old
select *   
from category c  
where c.CATEGORY_TYPE not in  (     
	select m.CATEGORY_ID       
	  from reservation r      
	 inner join category c on c.category_type = r.category_category_type
	 inner join model m on m.category_id = c.category_type
	where r.pickupdatetime <= '2019-06-23 08:30:00'  -- end
	  and r.dropoffdatetime >= '2019-06-22 17:30:00' -- start
)

