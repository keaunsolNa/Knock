import ModalWrapper from '@/components/ModalWrapper';
import styles from './layout.module.scss';

export default function Layout({ children, modal }: { children: React.ReactNode; modal: React.ReactNode }) {
  return (
    <>
      <div className={styles.container}>
        {children}
        <ModalWrapper modal={modal} />
      </div>
    </>
  );
}
