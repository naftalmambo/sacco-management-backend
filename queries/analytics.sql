SELECT
    m.member_id,
    (first_name || ' ' || last_name) AS member_name,
    account_type,
    balance,
    account_status
FROM
    members m
    INNER JOIN accounts a ON m.member_id = a.member_id
ORDER BY
    m.member_id;