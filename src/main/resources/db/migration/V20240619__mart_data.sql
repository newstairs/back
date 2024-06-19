/* 상품 이미지 추가 */
UPDATE product
SET product_img_url = CASE product_name
    WHEN '신라면(5개입)' THEN '/shinRamen.jpg'
    WHEN '진라면 매운맛(5개입)' THEN '/jinRamen.png'
    WHEN '해표 중력 밀가루(1kg)' THEN '/flour.jpg'
    WHEN '스페셜K 오리지널(480g)' THEN '/specialK.jpg'
    WHEN '탕종숙식빵(420g)' THEN '/bread.jpg'
    WHEN '3분 쇠고기카레(200g)' THEN '/3minCurry.jpg'
    WHEN '햇반 소프트밀 소고기죽(280g)' THEN '/beefPorridge.jpg'
    WHEN '팔도 왕뚜껑(110g)' THEN '/kingRamen.png'
    WHEN '농심 새우깡(90g)' THEN '/shrimpChip.jpeg'
    WHEN '닥터유 에너지바(40g)' THEN '/energyBar.jpg'
    WHEN '하리보 골드베렌 젤리(250g)' THEN '/jelly.jpg'
    WHEN '비비고 포기배추김치(3.3kg)' THEN '/kimchi.png'
    WHEN '오뚜기 옛날 자른미역(50g)' THEN '/seaweed.jpg'
    WHEN '오뚜기 연와사비(35g)' THEN '/wasabi.jpg'
    WHEN '하선정 까나리액젓(400g)' THEN '/fishSauce.jpg'
    WHEN '청정원 고소한 마요네즈(500g)' THEN '/mayonnaise.jpg'
    WHEN '동원 카놀라유(850ml)' THEN '/canolaOil.jpg'
    WHEN '오뚜기 콩기름(900ml)' THEN '/soybeanOil.jpg'
    WHEN '오뚜기 사과식초(900ml)' THEN '/appleVinegar.jpg'
    WHEN '딸기잼(500g)' THEN '/strawberryJam.jpg'
    WHEN '오뚜기카레 순한맛(100g)' THEN '/curry.jpg'
    ELSE product_img_url
END;