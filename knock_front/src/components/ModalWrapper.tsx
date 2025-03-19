'use client';
import { usePathname } from 'next/navigation';

export default function ModalWrapper({ modal }: { modal: React.ReactNode }) {
  const pathname = usePathname();
  const isSubmenuRoute = pathname.endsWith('/submenu');

  if (!isSubmenuRoute) return null; // submenu 경로가 아니면 모달 숨기기

  return <>{modal}</>;
}
