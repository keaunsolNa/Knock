import { motion, useAnimation } from 'framer-motion';
import { useEffect, useState } from 'react';
import styles from '@/styles/components/home/carousel.module.scss';

const images = [
  '/images/howto_1.png',
  '/images/howto_2.png',
  '/images/howto_3.png',
];

export default function Carousel() {
  const [imgIdx, setImgIdx] = useState(0);
  const controls = useAnimation();

  useEffect(() => {
    controls.start({ x: `-${imgIdx * 33}%` });
  }, [imgIdx, controls]);

  const handleDragEnd = (_, info) => {
    const offset = info.offset.x; // 드래그한 거리
    const direction = offset > 100 ? -1 : offset < -100 ? 1 : 0; // 일정 거리 이상이면 이동

    if (direction !== 0) {
      if (
        (imgIdx === 2 && direction === 1) ||
        (imgIdx === 0 && direction === -1)
      ) {
        // 끝에 도달했으면 원래 위치로 복귀
        controls.start({ x: `-${imgIdx * 33}%` });
      } else {
        setImgIdx((prev) => prev + direction);
      }
    } else {
      // 드래그 범위가 부족하면 원래 위치로 복귀
      controls.start({ x: `-${imgIdx * 33}%` });
    }
  };

  return (
    <section className={styles.section__carousel}>
      <motion.div
        className={styles.div__img_container}
        initial={{ x: `-${imgIdx * 33}%` }}
        animate={controls}
        transition={{ type: 'spring', stiffness: 500, damping: 30 }}
        drag="x"
        onDragEnd={handleDragEnd}
      >
        {images.map((imgSrc, idx) => (
          <img key={`howto_img_${idx}`} src={imgSrc} />
        ))}
      </motion.div>

      <div className={styles.div__dot_container}>
        {images.map((_, idx) => (
          <div
            key={`howto_dot_${idx}`}
            className={idx === imgIdx ? styles.div__cur_dot : null}
            onClick={() => setImgIdx(idx)}
          ></div>
        ))}
      </div>
    </section>
  );
}
