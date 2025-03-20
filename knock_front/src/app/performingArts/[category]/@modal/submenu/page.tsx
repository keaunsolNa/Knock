import CategoryNav from '@/components/performingArts/CategoryNav';
import styles from './page.module.scss';

export default async function Page() {
  return (
    <div className={styles.overlay}>
      <CategoryNav />
    </div>
  );
}
