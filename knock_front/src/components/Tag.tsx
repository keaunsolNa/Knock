export default function Tag({ text }: { text: string }) {
  return (
    <span
      style={{
        fontSize: '0.8rem',
        textAlign: 'center',
        padding: '5px 15px',
        backgroundColor: '#d2d2d2',
        borderRadius: '30px',
        color: '#353535',
      }}
    >
      {text}
    </span>
  );
}
