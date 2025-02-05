import styles from '@/styles/components/mypage/menu-item.module.scss';
import Link from 'next/link';
import { FaChevronRight } from 'react-icons/fa';

export default function MenuItem({
  name,
  value,
  link,
}: {
  name: string;
  value?: any;
  link: string;
}) {
  return (
    <Link href={link}>
      <div className={styles.container}>
        <div className={styles.div__col_name}>{name}</div>
        {value ? <div className={styles.div__col_value}>{value}</div> : null}
        <div className={styles.div__col_arrow}>
          <FaChevronRight />
        </div>
      </div>
    </Link>
  );
}
