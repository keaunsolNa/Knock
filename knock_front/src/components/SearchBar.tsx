'use client';

import styles from '@/styles/components/search-bar.module.scss';
import { IoSearchSharp } from 'react-icons/io5';
import { GrPowerReset } from 'react-icons/gr';

const mock = [
  { id: 'HLI1PJQBFU9yCJfDyn8z', nm: '판타지' },
  { id: 'HbI1PJQBFU9yCJfDy3_m', nm: '멜로/로맨스' },
  { id: 'FrI1PJQBFU9yCJfDv38x', nm: '드라마' },
  { id: 'IbI1PJQBFU9yCJfD0H-H', nm: '액션' },
  { id: 'F7I1PJQBFU9yCJfDwX9g', nm: 'SF' },
  { id: 'SbI2PJQBFU9yCJfDAYLB', nm: '뮤지컬' },
  { id: 'irI2PJQBFU9yCJfDOYht', nm: '서부극(웨스턴)' },
  { id: 'I7I1PJQBFU9yCJfD0n', nm: '다큐멘터리' },
  { id: 'G7I1PJQBFU9yCJfDyH9U', nm: '공연' },
];

export default function SearchBar() {
  return (
    <div className={styles.container}>
      <div className={styles.div__title}>
        <div>
          <input placeholder="영화명으로 검색해보세요" />
          <IoSearchSharp />
        </div>
        <button>
          <GrPowerReset />
        </button>
      </div>

      <div className={styles.div__category}>
        <div className={styles.list}>
          {mock.map((item, idx) => (
            <span
              key={item.id}
              className={`${styles.item} ${idx === 1 ? styles.item_select : ''}`}
            >
              {item.nm}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
}
