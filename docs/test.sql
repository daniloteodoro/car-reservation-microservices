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

